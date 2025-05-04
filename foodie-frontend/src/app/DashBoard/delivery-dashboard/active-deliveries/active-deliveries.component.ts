import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface ActiveDelivery {
  id: number;
  orderId: number;
  restaurantName: string;
  restaurantAddress: string;
  customerName: string;
  customerAddress: string;
  deliveryStatus: string;
}

@Component({
  selector: 'app-active-deliveries',
  imports: [CommonModule],
  templateUrl: './active-deliveries.component.html',
  styleUrls: ['./active-deliveries.component.css']
})
export class ActiveDeliveriesComponent implements OnInit {


  userId: number = 0; // Replace with actual user ID
  activeDeliveries: ActiveDelivery[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.userId=Number(localStorage.getItem('userId'));
    this.fetchActiveDeliveries();
  }

  fetchActiveDeliveries(): void {
    this.http.get<any[]>(`http://localhost:8080/order/activeDeliveries/${this.userId}`)
      .subscribe(data => {
        this.activeDeliveries = data;
      });
  }

  updateStatus(deliveryId: number, newStatus: string): void {
    this.http.put(`http://localhost:8080/order/updateDeliveries/${deliveryId}`, { status: newStatus })
      .subscribe(() => {
        this.fetchActiveDeliveries(); // Refresh list after update
      });
  }
}
