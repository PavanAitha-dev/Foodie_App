import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HeaderComponent } from '../../components/header/header.component';

@Component({
  selector: 'app-restaurant-dashboard',
  imports: [RouterModule,HeaderComponent], 
  templateUrl: './restaurant-dashboard.component.html',
  styleUrls: ['./restaurant-dashboard.component.css']
})
export class RestaurantDashboardComponent implements OnInit {

  fullName: string | null = '';
  restaurantName: string | null = '';

  ngOnInit(): void {
    this.fullName = localStorage.getItem('fullName');
    this.restaurantName = localStorage.getItem('restaurantName');
  }
}
