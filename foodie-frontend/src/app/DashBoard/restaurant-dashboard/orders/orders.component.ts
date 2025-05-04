import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
  orders: any[] = [];
  restaurantId: string | null = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.restaurantId = localStorage.getItem('restaurantId');
    this.fetchOrders();
  }

  fetchOrders(): void {
    if (this.restaurantId) {
      this.http.get(`http://localhost:8080/order/getAllOrders/${this.restaurantId}`)//http://localhost:8080/order/getAllOrders/13
        .subscribe((data: any) => {
          this.orders = data;
        });
    }
  }

  updateStatus(orderId: number, newStatus: string): void {
    this.http.put(`http://localhost:8080/order/updateStatus/${orderId}`, {status: newStatus })//updateStatus/{id}
      .subscribe(() => {
        this.fetchOrders(); // Refresh after update
      });
  }
}
