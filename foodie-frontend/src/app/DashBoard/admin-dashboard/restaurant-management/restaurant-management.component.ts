// restaurant-management.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-restaurant-management',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './restaurant-management.component.html',
  styleUrls: ['./restaurant-management.component.css']
})
export class RestaurantManagementComponent implements OnInit {

  restaurants: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadRestaurants();
  }

  loadRestaurants(): void {
    console.log('Loading restaurants...');
    this.http.get<any[]>('http://localhost:8080/restaurant/all')
      .subscribe((data) => this.restaurants = data);
  }
  deleteRestaurant(id: number) {
    if (confirm('Are you sure you want to delete this restaurant?')) {
      this.http.delete(`http://localhost:8080/restaurant/delete/${id}`, { responseType: 'text' })
        .subscribe(() => {
          console.log('Restaurant deleted successfully');
          this.loadRestaurants();
    });
    }
  }
  updateStatus(id: number, newStatus: string): void {
    this.http.put(`http://localhost:8080/restaurant/status/${id}`, { status: newStatus })
      .subscribe(() => this.loadRestaurants());
  }
  
  
}
