import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CommentsResponse } from '../interface/api/commentsResponse.interface';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private pathService = 'http://localhost:8080/api/comments';
  
  constructor(private httpClient: HttpClient) { }

  public addComment(id: string): Observable<CommentsResponse> {
    return this.httpClient.get<CommentsResponse>(`${this.pathService}/${id}`);
  }

}
