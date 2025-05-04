import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface AvailableOrder {
  orderId: number;
  restaurantName: string;
  restaurantAddress: string;
  customerName: string;
  customerAddress: string;
  deliveryPersonId: number;
  deliveryStatus: string;

}

@Component({
  selector: 'app-available-orders',
  imports: [CommonModule],
  templateUrl: './available-orders.component.html',
  styleUrls: ['./available-orders.component.css']
})
export class AvailableOrdersComponent implements OnInit {
   userId: number = 0; // Replace with actual user ID
  availableOrders: AvailableOrder[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.userId=Number(localStorage.getItem('userId'));
    this.fetchAvailableOrders();
  }

  fetchAvailableOrders(): void {
    this.http.get<any[]>('http://localhost:8080/order/getActiveDeliveries')
      .subscribe(data => {
        this.availableOrders = data;
      });
  }

  acceptOrder(order : AvailableOrder): void {
    order.deliveryPersonId = this.userId; // Assign the delivery person ID to the order
    console.log(order);
    this.http.post(`http://localhost:8080/order/addDelivery`,order)
      .subscribe(() => {
        this.fetchAvailableOrders(); // refresh list
      });
  }
}
