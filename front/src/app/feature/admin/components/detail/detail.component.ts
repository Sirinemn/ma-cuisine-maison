import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdminUserService } from '../../service/admin-user.service';
import { User } from '../../../../interface/user';
import { DatePipe } from '@angular/common';
import { MatCardModule } from '@angular/material/card';


@Component({
  selector: 'app-detail',
  standalone: true,
  imports: [
    DatePipe,
    MatCardModule
  ],
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.scss'
})
export class DetailComponent {
  
  user: User | undefined;

  constructor(private route: ActivatedRoute, private userService: AdminUserService) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id')!;
    this.userService.getUserById(id).subscribe(user => {
      this.user = user;
    });
  }
}  


