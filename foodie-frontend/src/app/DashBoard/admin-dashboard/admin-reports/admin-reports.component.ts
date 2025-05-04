import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-admin-reports',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-reports.component.html',
  styleUrls: ['./admin-reports.component.css']
})
export class AdminReportsComponent implements OnInit {

  totalOrders = 0;
  totalRevenue = 0;
  topRestaurants: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadReports();
  }

  loadReports() {
    this.http.get<any>('http://localhost:8080/orders/report').subscribe(data => {
      this.totalOrders = data.totalOrders;
      this.totalRevenue = data.totalRevenue;
      this.topRestaurants = data.topRestaurants;
    });
  }
}
