import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../../components/header/header.component';
import { CommonModule } from '@angular/common';
import { ActiveDeliveriesComponent } from './active-deliveries/active-deliveries.component';
import { AvailableOrdersComponent } from './available-orders/available-orders.component';
import { DeliveryHistoryComponent } from './delivery-history/delivery-history.component';

@Component({
  selector: 'app-delivery-dashboard',
  imports: [HeaderComponent,CommonModule,ActiveDeliveriesComponent,AvailableOrdersComponent,DeliveryHistoryComponent], // No need to import RouterModule here as it's not used in this component
  templateUrl: './delivery-dashboard.component.html',
  styleUrls: ['./delivery-dashboard.component.css']
})
export class DeliveryDashboardComponent implements OnInit {

  fullName: string | null = '';
  activeTab: string = 'available';

  ngOnInit(): void {
    this.fullName = localStorage.getItem('fullName');
  }
}
