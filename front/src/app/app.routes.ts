import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { NotFoundComponent } from './pages/not-found/not-found.component';
import { UnauthGuard } from './guards/unauth.guard';
import { AuthGuard } from './guards/auth.guard';
import { MeComponent } from './components/me/me.component';

export const routes: Routes = [
  { 
    path: '',
     component: HomeComponent ,
     canActivate: [UnauthGuard]
  },
  {
    path: 'me',
    component: MeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'auth',
    canActivate: [UnauthGuard], 
    loadChildren : ()=> 
      import('./feature/auth/auth.routes').then((r) => r.auth_routes) 
  },
  {
    path: 'admin',
    canActivate: [AuthGuard], 
    loadChildren : ()=> 
      import('./feature/admin/admin.routes').then((r) => r.admin_routes) 
  },
  {
    path: 'reception',
    canActivate: [AuthGuard], 
    loadChildren : ()=> 
      import('./feature/reception/reception.routes').then((r) => r.reception_routes) 
  },
  { 
    path: '404',
    component: NotFoundComponent 
  },
  { 
    path: '**',
    redirectTo: '404' 
  },
];

