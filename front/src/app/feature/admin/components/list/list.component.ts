import { Component, OnInit } from '@angular/core';
import { User } from '../../../../interface/user'
import { AdminUserService } from '../../service/admin-user.service'
import { Router } from '@angular/router';
import { CommonModule, NgFor } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';


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
export class ListComponent implements OnInit {
  users = new MatTableDataSource<User>();
  displayedColumns: string[] = ['pseudo', 'email', 'actions'];
  constructor(private userService: AdminUserService, private router: Router) {}

  ngOnInit(): void {
    this.userService.getAllUsers().subscribe(users => {
      this.users.data = users;
    });
  }
  viewUser(id: number): void {
    this.router.navigate([`/user/${id}`]);
  }

  editUser(id: number): void {
    this.router.navigate([`/user/${id}`]);
  }

  deleteUser(id: number): void {
    this.userService.deleteUser(id).subscribe(() => {
      this.users.data = this.users.data.filter(user => user.id !== id);
    });
  }

}
