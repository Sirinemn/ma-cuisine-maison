import { Component, OnDestroy, OnInit } from '@angular/core';
import { User } from '../../../../interface/user'
import { AdminUserService } from '../../service/admin-user.service'
import { Router } from '@angular/router';
import { CommonModule, NgFor } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../../../components/confirm-dialog/confirm-dialog.component';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-list',
  standalone: true,
  imports: [
    NgFor,
    MatIconModule,
    CommonModule,
    MatTableModule,
    MatButtonModule
  ],
  templateUrl: './list.component.html',
  styleUrl: './list.component.scss'
})
export class ListComponent implements OnInit, OnDestroy {

  private httpSubscriptions: Subscription[] = [];
  users = new MatTableDataSource<User>();
  displayedColumns: string[] = ['pseudo', 'email', 'actions'];
  constructor(private adminService: AdminUserService,
     private router: Router,
     public dialog: MatDialog) {}

  ngOnInit(): void {
    this.httpSubscriptions.push(this.adminService.getAllUsers().subscribe(users => {
      this.users.data = users;
    }));
  }
  public back() {
    window.history.back();
  }
  viewUser(id: number): void {
    this.router.navigate([`admin/user/${id}/view`]);
  }

  editUser(id: number): void {
    this.router.navigate([`admin/user/${id}/edit`]);
  }

  deleteUser(id: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.httpSubscriptions.push(this.adminService.deleteUser(id).subscribe(() => {
          this.users.data = this.users.data.filter(user => user.id !== id);
        }));
      }
    });
  }
  ngOnDestroy(): void {
    this.httpSubscriptions.forEach(subscribtion=> subscribtion.unsubscribe());
  } 
}
