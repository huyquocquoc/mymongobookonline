import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet],
  template: `
    <nav class="app-shell-nav">
      <a routerLink="/">Home</a>
      <a routerLink="/login">Login</a>
    </nav>
    <router-outlet></router-outlet>
  `,
  styles: [
    `
      .app-shell-nav {
        display: flex;
        justify-content: flex-end;
        gap: 18px;
        padding: 18px 24px;
        background: #f8fafc;
        border-bottom: 1px solid #e2e8f0;
      }
      .app-shell-nav a {
        color: #1d4ed8;
        text-decoration: none;
        font-weight: 700;
      }
      .app-shell-nav a:hover {
        text-decoration: underline;
      }
    `
  ]
})
export class AppComponent {}
