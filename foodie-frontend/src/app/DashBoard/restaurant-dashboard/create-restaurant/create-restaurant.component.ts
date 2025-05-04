import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-restaurant',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  styleUrls: ['./create-restaurant.component.css'],
  templateUrl: './create-restaurant.component.html'

})
export class CreateRestaurantComponent {
  restaurantForm: FormGroup;
  constructor(private fb: FormBuilder, private http: HttpClient, private router: Router) {


    console.log('CreateRestaurantComponent initialized');
  this.restaurantForm = this.fb.group({
    name: [''],
    address: [''],
    phone: [''],
    contactNumber: [''],
    openingHours: [''],
  });

  }

  onSubmit() {
    const email = localStorage.getItem('email');
    const data = { ...this.restaurantForm.value, email };
    this.http.post('http://localhost:8080/restaurant/addRest', data)
    .subscribe({
      next: (restaurant: any) => {
        alert('Restaurant created!');
        localStorage.setItem('restaurantId', restaurant.id);
        localStorage.setItem('restaurantName', restaurant.name);
        this.router.navigate(['/restaurant-dashboard']);
      }});
  }
}
