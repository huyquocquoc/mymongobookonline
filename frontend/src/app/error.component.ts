import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ErrorService } from './error.service';

@Component({
  selector: 'app-error',
  standalone: true,
  imports: [CommonModule],
  template: `
    <main class="error-page">
      <div class="error-container">
        <h1 class="error-title">{{ error()?.statusCode || 'Error' }}</h1>
        <p class="error-message">{{ error()?.message || 'An unexpected error occurred' }}</p>
        <p class="error-timestamp">{{ error()?.timestamp | date:'medium' }}</p>
        <div class="error-actions">
          <button type="button" class="primary" (click)="returnToPrevious()">Go Back</button>
          <button type="button" (click)="goHome()">Go Home</button>
        </div>
      </div>
    </main>
  `,
  styles: [`
    .error-page {
      display: flex;
      align-items: center;
      justify-content: center;
      min-height: 100vh;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
        'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Source Sans Pro',
        sans-serif;
      padding: 20px;
    }

    .error-container {
      background: white;
      border-radius: 12px;
      padding: 48px 32px;
      box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
      text-align: center;
      max-width: 500px;
    }

    .error-title {
      font-size: 72px;
      font-weight: 700;
      margin: 0 0 16px;
      color: #667eea;
    }

    .error-message {
      font-size: 18px;
      color: #333;
      margin: 0 0 12px;
      line-height: 1.6;
    }

    .error-timestamp {
      font-size: 12px;
      color: #999;
      margin: 0 0 32px;
    }

    .error-actions {
      display: flex;
      gap: 12px;
      justify-content: center;
    }

    button {
      padding: 12px 24px;
      border: none;
      border-radius: 6px;
      font-size: 14px;
      font-weight: 600;
      cursor: pointer;
      transition: all 0.3s ease;
    }

    button.primary {
      background: #667eea;
      color: white;
    }

    button.primary:hover {
      background: #5568d3;
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
    }

    button {
      background: #f0f0f0;
      color: #333;
    }

    button:hover {
      background: #e0e0e0;
      transform: translateY(-2px);
    }
  `]
})
export class ErrorComponent {
  error = this.errorService.currentError;

  constructor(private errorService: ErrorService) {}

  returnToPrevious() {
    this.errorService.returnToPreviousPage();
  }

  goHome() {
    this.errorService.clearError();
    window.location.href = '/';
  }
}
