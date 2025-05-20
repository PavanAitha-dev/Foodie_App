import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient} from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [CommonModule, ReactiveFormsModule,RouterModule],
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }
  ngOnInit(): void {
    localStorage.clear();
  }

  onSubmit(): void {
  if (this.loginForm.valid) {
    this.http.post('http://localhost:8080/user/auth/login', this.loginForm.value)
      .subscribe({
        next: (res: any) => {
          localStorage.clear();
          localStorage.setItem('email', res.email);
          localStorage.setItem('roleId', res.roleID);
          localStorage.setItem('fullName', res.fullName);
          localStorage.setItem('userId', res.userID);
          localStorage.setItem('token', res.token); // Store the JWT token

          if (res.roleID === 2) {
            this.http.get(`http://localhost:8080/restaurant/by-user/${res.email}`).subscribe({
              next: (restaurant: any) => {
                if (!restaurant || Object.keys(restaurant).length === 0) {
                  // If no restaurant found or empty response
                  console.log('kk initialized');

                  this.router.navigate(['/restaurant-dashboard/create-restaurant']);

                } else {
                  // Restaurant found → go to dashboard
                  localStorage.setItem('restaurantId', restaurant.id);
                  localStorage.setItem('restaurantName', restaurant.name);
                  this.router.navigate(['/restaurant-dashboard']);
                }
              },
              error: (err) => {
                console.error('Error fetching restaurant:', err);
                this.router.navigate(['/create-restaurant']);
              }
            });          
          } else if (res.roleID === 1) {
            this.router.navigate(['/admin-dashboard']);
          } else if (res.roleID === 3) {
            this.router.navigate(['/customer-dashboard']);
          } else if (res.roleID === 4) {
            this.router.navigate(['/delivery-dashboard']);
          }
        },
        error: (res) => {
          alert(res.error.message);
        }
      });
  }
}

}
