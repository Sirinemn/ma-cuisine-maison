import { Routes } from '@angular/router';
import { WelcomeComponent } from './welcome/welcome.component';


export const reception_routes: Routes = [

  { 
    title: 'Reception',
    path: 'welcome', 
    component: WelcomeComponent 
  },
];