import { Component, OnDestroy, OnInit } from '@angular/core';
import { RecipeService } from '../../service/recipe.service';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { Recipe } from '../../interface/recipe';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { CommentService } from '../../service/comment.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SessionService } from '../../../../service/session.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Comment } from '../../interface/comment.interface';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../../../components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-recipe-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIcon,
    RouterModule,
    MatFormFieldModule,
    CommonModule,
    ReactiveFormsModule,
    MatInputModule
   ],
  templateUrl: './recipe-detail.component.html',
  styleUrl: './recipe-detail.component.scss'
})
export class RecipeDetailComponent implements OnInit, OnDestroy{
  private httpSubscriptions: Subscription[] = [];
  public recipe!: Recipe;
  public comments: Comment[] = [] ;
  public commentForm: FormGroup;
  public showCommentsSection: boolean = false;
  public selectedCommentId: number | null = null;

  constructor(
    private recipeService: RecipeService,
    private activateRoute: ActivatedRoute,
    private commentService: CommentService,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private sessionService: SessionService
  ){
    this.commentForm = this.formBuilder.group({
      content: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    const id =+ this.activateRoute.snapshot.paramMap.get('id')!;
    this.httpSubscriptions.push(
      this.recipeService.getRecipeById(id).subscribe(
        resultat => this.recipe = resultat
      )
    );
    console.log(this.selectedCommentId);
  }
  toggleComments(): void {
    this.showCommentsSection = !this.showCommentsSection;
    if (this.showCommentsSection) {
      this.loadComments(); // Only load comments when showing for the first time
    }
  }
  loadComments(): void {
    const recipeId = this.recipe?.id?.toString();
    if (recipeId) {
      this.httpSubscriptions.push(
        this.commentService.getRecipeComments(recipeId).subscribe(
          (resultat) => {
            this.comments = resultat?.comments || []; 
          },
          (error) => {
            console.error('Failed to load comments', error);
            this.comments = []; 
          }
        )
      );
    }
  }
  
  addComment(): void {
    if (this.commentForm.valid && this.recipe) {
      const comment = {
        content: this.commentForm.get('content')?.value,
        recipeId: this.recipe.id!,
        userId: this.sessionService.user?.id!,
        userPseudo: this.sessionService.user?.pseudo!
      };
  
      if (this.selectedCommentId) {
        // Update existing comment
        this.httpSubscriptions.push(
          this.commentService.updateComment(this.selectedCommentId.toString(), comment.content).subscribe(
            response => {
              this.snackBar.open(response.message, 'ok', { duration: 2000 });
              this.loadComments(); // Reload comments after updating
              this.commentForm.reset(); // Reset form
              this.selectedCommentId = null; // Clear the selected comment ID
            },
            error => {
              this.snackBar.open('Failed to update comment', 'ok', { duration: 2000 });
            }
          )
        );
      } else {
        // Add new comment
        this.httpSubscriptions.push(
          this.commentService.addComment(comment).subscribe(
            response => {
              this.snackBar.open(response.message, 'ok', { duration: 2000 });
              this.loadComments(); // Reload comments after adding new one
              this.commentForm.reset(); // Reset form
            },
            error => {
              this.snackBar.open('Failed to add comment', 'ok', { duration: 2000 });
            }
          )
        );
      }
    }
  }
  canEditOrDelete(comment: Comment): boolean {
    const currentUser = this.sessionService.user;
    if (!currentUser) {
      return false;
    }
    // Vérifier si l'utilisateur est l'auteur du commentaire ou admin
    const isAuthor = comment.userId === currentUser.id;
    const isAdmin = currentUser.roles.includes('ADMIN');
  
    return isAuthor || isAdmin;
  }
  deleteComment( comment: Comment ) {
    const dialogRef = this.dialog.open(ConfirmDialogComponent);
    dialogRef.afterClosed().subscribe(() => {
      this.httpSubscriptions.push(this.commentService.deleteComment(comment.id?.toString()!).subscribe(
        response => {
          this.snackBar.open("Comment deleted with success", 'ok', { duration: 2000 });
          this.loadComments();
        },
        error => {
          this.snackBar.open('Failed to delete comment', 'ok', { duration: 2000 });
        }
      ));
    })  
  }
  editComment(comment: Comment): void {
    this.commentForm.setValue({
      content: comment.content
    });
  
    this.selectedCommentId = comment.id!;
  }
  ngOnDestroy(): void {
    this.httpSubscriptions.forEach( sub => sub.unsubscribe());
  }

  public imgError(event: Event): void {
    const element = event.target as HTMLImageElement;
    element.src = 'assets/default-image.jpg'; // Chemin vers une image par défaut
  }  
  public back() {
    window.history.back();
  }
}
