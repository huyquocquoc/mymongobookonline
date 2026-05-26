import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { ErrorComponent } from './error.component';

export const routes: Routes = [
  {
    path: '',
    component: AppComponent
  },
  {
    path: 'error',
    component: ErrorComponent
  },
  {
    path: '**',
    redirectTo: '/'
  }
];
