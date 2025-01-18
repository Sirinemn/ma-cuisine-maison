import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CommentsResponse } from '../interface/api/commentsResponse.interface';
import { Comment } from '../interface/comment.interface';
import { MessageResponse } from '../../../interface/api/messageResponse.interface';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private pathService = 'http://localhost:8080/api/comments';

  constructor(private httpClient: HttpClient) {}

  public getRecipeComments(id: string): Observable<CommentsResponse> {
    return this.httpClient.get<CommentsResponse>(`${this.pathService}/${id}`);
  }

  public addComment(comment: Comment): Observable<MessageResponse> {
    return this.httpClient.post<MessageResponse>(`${this.pathService}`, comment);
  }
}

