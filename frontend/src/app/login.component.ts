import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = '';
  password = '';
  error = '';

  constructor(private readonly router: Router) {}

  submit(): void {
    if (!this.email.trim() || !this.password) {
      this.error = 'Please enter both email and password.';
      return;
    }

    this.error = '';
    this.router.navigate(['/']);
  }
}
