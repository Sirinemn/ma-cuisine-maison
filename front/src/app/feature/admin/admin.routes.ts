import { Routes } from "@angular/router";
import { AuthGuard } from '../../guards/auth.guard'
import { ListComponent } from "./components/list/list.component";
import { DetailComponent } from '../admin/components/detail/detail.component'

export const admin_routes: Routes = [
    {
        title: 'List',
        path: 'users-list',
        component: ListComponent,
      },
      {
        title: 'Detail',
        path: 'user/:id',
        canActivate: [AuthGuard],
        component: DetailComponent,
      },
]