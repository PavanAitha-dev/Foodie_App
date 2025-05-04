import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface DeliveryRecord {
  id: number;
  orderId: number;
  restaurantName: string;
  restaurantAddress: string;
  customerName: string;
  customerAddress: string;
  deliveryStatus: string;
}

@Component({
  selector: 'app-delivery-history',
  imports: [CommonModule],
  templateUrl: './delivery-history.component.html',
  styleUrls: ['./delivery-history.component.css']
})
export class DeliveryHistoryComponent implements OnInit {

  deliveryHistory: DeliveryRecord[] = [];
  userId: number = 0; // Replace with actual user ID

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.userId = Number(localStorage.getItem('userId'));
    this.fetchHistory();
  }

  fetchHistory(): void {
    this.http.get<any[]>(`http://localhost:8080/order/allDeliveriesById/${this.userId}`)
      .subscribe(data => {
        this.deliveryHistory = data;
      });
  }
}
