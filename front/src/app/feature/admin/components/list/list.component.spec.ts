import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListComponent } from './list.component';
import { AdminUserService } from '../../service/admin-user.service';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { of } from 'rxjs';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let mockAdminService: Partial<AdminUserService>;
  let mockDialog: Partial<MatDialog>;
  let mockRouter: Partial<Router>;

  beforeEach(async () => {
    mockAdminService = {
      getAllUsers: jest.fn().mockReturnValue(of([
        { id: 1, pseudo: 'user1', email: 'user1@example.com' },
        { id: 2, pseudo: 'user2', email: 'user2@example.com' }
      ])),
      deleteUser: jest.fn().mockReturnValue(of({}))
    };

    mockDialog = {
      open: jest.fn().mockReturnValue({
        afterClosed: () => of(true)
      })
    };

    mockRouter = {
      navigate: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [ListComponent],
      providers: [
        { provide: AdminUserService, useValue: mockAdminService },
        { provide: MatDialog, useValue: mockDialog },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load users on init', () => {
    component.ngOnInit();
    expect(mockAdminService.getAllUsers).toHaveBeenCalled();
    expect(component.users.data.length).toBe(2);
    expect(component.users.data).toEqual([
      { id: 1, pseudo: 'user1', email: 'user1@example.com' },
      { id: 2, pseudo: 'user2', email: 'user2@example.com' }
    ]);
  });

  it('should navigate to view user', () => {
    const userId = 1;
    component.viewUser(userId);
    expect(mockRouter.navigate).toHaveBeenCalledWith([`admin/user/${userId}/view`]);
  });

  it('should navigate to edit user', () => {
    const userId = 1;
    component.editUser(userId);
    expect(mockRouter.navigate).toHaveBeenCalledWith([`admin/user/${userId}/edit`]);
  });

  it('should delete a user after confirmation', () => {
    component.deleteUser(1);
    
    expect(mockDialog.open).toHaveBeenCalled();  // Vérifie que le dialogue a été ouvert
    expect(mockAdminService.deleteUser).toHaveBeenCalledWith(1);  // Vérifie que le service est appelé après confirmation
  });

  it('should not delete a user if dialog is closed without confirmation', () => {
    // Simule le fait que le dialogue soit fermé sans confirmation
    (mockDialog.open as jest.Mock).mockReturnValueOnce({
      afterClosed: () => of(false)
    });

    component.deleteUser(1);
    
    expect(mockDialog.open).toHaveBeenCalled();
    expect(mockAdminService.deleteUser).not.toHaveBeenCalled();  // Le service ne doit pas être appelé si l'utilisateur annule
  });
});
