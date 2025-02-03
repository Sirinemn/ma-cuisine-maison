import { TestBed } from '@angular/core/testing';
import { CommentService } from './comment.service';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { Comment } from '../interface/comment.interface';
import { MessageResponse } from '../../../interface/api/messageResponse.interface';

describe('CommentService', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;

  const commentMock: Comment = {
    id: 1,
    content: "this is a comment",
    recipeId: 1,
    userId: 1,
    userPseudo: "pseudo"
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClientTesting(), 
        provideHttpClient(),        
      ],
    });
    service = TestBed.inject(CommentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Vérification qu'aucune requête n'est en attente
  });

  xit('should return comments by recipe id', () => {
    const commentListMock = [commentMock];

    service.getRecipeComments("1").subscribe(response => {
      expect(response).toEqual(commentListMock); 
    });

    const req = httpMock.expectOne(`${service['pathService']}/1`);
    expect(req.request.method).toBe('GET'); 
    req.flush(commentListMock); 
  });

  xit('should add a new comment', () => {
    const messageResponse: MessageResponse = { message: 'Comment added with success!' };

    service.addComment(commentMock).subscribe(response => {
      expect(response.message).toEqual('Comment added with success!');
    });

    const req = httpMock.expectOne(`${service['pathService']}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(commentMock); 
    req.flush(messageResponse);
  });

  xit('should delete a comment', () => {
    service.deleteComment("1").subscribe(response => {
      expect(response).toBeNull(); // Ou toute autre vérification appropriée
    });
    const req = httpMock.expectOne(`${service['pathService']}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  xit('should update a comment', () => {
    const messageResponse: MessageResponse = { message: 'Comment updated with success!' };

    service.updateComment("1", "Updated content").subscribe(response => {
      expect(response.message).toEqual('Comment updated with success!');
    });

    const req = httpMock.expectOne(`${service['pathService']}/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual("Updated content");
    req.flush(messageResponse);
  });

  it('should be created', () => {
    expect(service).toBeTruthy(); 
  });
});
