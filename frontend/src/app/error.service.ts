import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';

export interface ErrorInfo {
  message: string;
  statusCode?: number;
  timestamp: Date;
  returnUrl?: string;
}

@Injectable({ providedIn: 'root' })
export class ErrorService {
  readonly currentError = signal<ErrorInfo | null>(null);

  constructor(private router: Router) {}

  setError(error: Omit<ErrorInfo, 'timestamp'>) {
    this.currentError.set({
      ...error,
      timestamp: new Date(),
      returnUrl: this.router.url
    });
  }

  clearError() {
    this.currentError.set(null);
  }

  redirectToError(error: Omit<ErrorInfo, 'timestamp'>) {
    this.setError(error);
    this.router.navigate(['/error']);
  }

  returnToPreviousPage() {
    const returnUrl = this.currentError()?.returnUrl;
    if (returnUrl && returnUrl !== '/error') {
      this.clearError();
      this.router.navigateByUrl(returnUrl);
    } else {
      this.clearError();
      this.router.navigate(['/']);
    }
  }
}
