import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-orders.component.html',
  styleUrls: ['./customer-orders.component.css']
})
export class CustomerOrdersComponent implements OnInit {
  orders: any[] = [];
  UserId: string | null = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.UserId = localStorage.getItem('userId');
    this.fetchOrders();
  }

  fetchOrders(): void {
    if (this.UserId) {
      this.http.get(`http://localhost:8080/order/getOrdersCustId/${this.UserId}`)
        .subscribe((data: any) => {
          this.orders = data;
        });
    }
  }
submitRating(order: any, rating: number): void {
    order.rating = rating;  // Update the order rating
    console.log(`Order ID: ${order.id}, Rating: ${order.rating}`);
    this.http.put(`http://localhost:8080/order/updateRating/${order.id}`, order.rating)
        .subscribe((data: any) => {
          console.log('Rating updated successfully:', data);
          this.fetchOrders(); // Refresh the orders after updating the rating
        });
}




}
