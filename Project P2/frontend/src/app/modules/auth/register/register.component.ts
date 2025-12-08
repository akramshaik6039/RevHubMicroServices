import { Component, OnInit } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService, RegisterRequest } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  displayText = '';
  fullText = 'RevHub';
  showForm = false;
  
  registerData: RegisterRequest = {
    username: '',
    email: '',
    password: ''
  };
  
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}
  
  ngOnInit() {
    this.typeText();
  }
  

  
  typeText() {
    let i = 0;
    const interval = setInterval(() => {
      this.displayText = this.fullText.substring(0, i + 1);
      i++;
      if (i >= this.fullText.length) {
        clearInterval(interval);
        setTimeout(() => {
          this.showForm = true;
        }, 500);
      }
    }, 300);
  }
  
  onSubmit() {
    if (this.registerData.username && this.registerData.email && this.registerData.password) {
      this.isLoading = true;
      this.errorMessage = '';
      this.successMessage = '';
      
      this.authService.register(this.registerData).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.successMessage = response.message || 'Registration successful!';
          setTimeout(() => {
            this.router.navigate(['/auth/verify-otp'], { queryParams: { email: this.registerData.email } });
          }, 2000);
        },
        error: (error) => {
          this.isLoading = false;
          console.error('Registration error:', error);
          
          if (error.error && error.error.error) {
            this.errorMessage = error.error.error;
          } else if (error.error && typeof error.error === 'string') {
            this.errorMessage = error.error;
          } else if (error.error && error.error.message) {
            this.errorMessage = error.error.message;
          } else if (error.status === 0) {
            this.errorMessage = 'Cannot connect to server. Please check if backend is running.';
          } else {
            this.errorMessage = 'Registration failed. Please try again.';
          }
        }
      });
    }
  }
}
