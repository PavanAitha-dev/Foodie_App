import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-restaurant-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './restaurant-profile.component.html',
  styleUrls: ['./restaurant-profile.component.css']
})
export class RestaurantProfileComponent implements OnInit {
  profileForm!: FormGroup;
  restaurantId: string | null = '';
  email: string | null =''
  loading: boolean = true;
  constructor(private fb: FormBuilder, private http: HttpClient) {}

  ngOnInit(): void {
    this.restaurantId = localStorage.getItem('restaurantId');
    this.email = localStorage.getItem('email');


    this.profileForm = this.fb.group({
      name: ['', Validators.required],
      address: ['', Validators.required],
      contactNumber: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      openingHours: ['', Validators.required],
      // averageRating: ['', [Validators.required, Validators.min(0), Validators.max(5)]],
      // status: [''],
    });

    this.loadRestaurantData();
  }

  loadRestaurantData(): void {
    if (this.restaurantId) {
      this.http.get<any>(`http://localhost:8080/restaurant/by-user/${this.email}`).subscribe({
        next: (res) => {
          this.profileForm.patchValue(res);
          this.loading = false;
        },
        error: () => {
          alert('Failed to load restaurant profile.');
          this.loading = false;
        }
      });
    }
  }

  onSubmit(): void {
    if (this.profileForm.valid && this.restaurantId) {
      this.http.put(`http://localhost:8080/restaurant/update/${this.restaurantId}`, this.profileForm.value)
        .subscribe({
          next: () => alert('Profile updated successfully!'),
          error: () => alert('Update failed. Please try again.')
        });
    }
  }
}
