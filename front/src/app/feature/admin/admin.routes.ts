import { Routes } from "@angular/router";
import { ListComponent } from "./components/list/list.component";
import { DetailComponent } from '../admin/components/detail/detail.component'
import { FormComponent } from "./components/form/form.component";

export const admin_routes: Routes = [
    {
        title: 'List',
        path: 'users-list',
        component: ListComponent,
      },
      {
        title: 'Detail',
        path: 'user/:id/view',
        component: DetailComponent,
      },
      {
        title: 'Form',
        path: 'user/:id/edit',
        component: FormComponent,
      }
]