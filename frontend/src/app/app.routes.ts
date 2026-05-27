import { Routes } from '@angular/router';
import { ErrorComponent } from './error.component';
import { HomeComponent } from './home.component';
import { LoginComponent } from './login.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'error',
    component: ErrorComponent
  },
  {
    path: '**',
    redirectTo: ''
  }
];
