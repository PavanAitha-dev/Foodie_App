// app.routes.ts
import { Routes } from '@angular/router';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AdminDashboardComponent } from './DashBoard/admin-dashboard/admin-dashboard.component';
import { RestaurantDashboardComponent } from './DashBoard/restaurant-dashboard/restaurant-dashboard.component';
import { CustomerDashboardComponent } from './DashBoard/customer-dashboard/customer-dashboard.component';
import { DeliveryDashboardComponent } from './DashBoard/delivery-dashboard/delivery-dashboard.component';
import { UserManagementComponent } from './DashBoard/admin-dashboard/user-management/user-management.component';
import { RestaurantManagementComponent } from './DashBoard/admin-dashboard/restaurant-management/restaurant-management.component';
import { AdminReportsComponent } from './DashBoard/admin-dashboard/admin-reports/admin-reports.component';
import { CreateRestaurantComponent } from './DashBoard/restaurant-dashboard/create-restaurant/create-restaurant.component';
import { MenuComponent } from './DashBoard/restaurant-dashboard/menu/menu.component';
import { OrdersComponent } from './DashBoard/restaurant-dashboard/orders/orders.component';
import { RestaurantProfileComponent } from './DashBoard/restaurant-dashboard/restaurant-profile/restaurant-profile.component';
import { RestaurantMenuComponent } from './DashBoard/customer-dashboard/restaurant-menu/restaurant-menu.component';
import { OrderSummaryComponent } from './DashBoard/customer-dashboard/order-summary/order-summary.component';
import { PaymentComponent } from './DashBoard/customer-dashboard/payment/payment.component';
import { CustomerOrdersComponent } from './DashBoard/customer-dashboard/customer-orders/customer-orders.component';
import { CustomerAddressComponent } from './DashBoard/customer-dashboard/customer-address/customer-address.component';


export const routes: Routes = [
  { path: '', component: WelcomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'admin-dashboard', component: AdminDashboardComponent,
    children:[
      { path: 'users', component: UserManagementComponent },
      { path: 'restaurants', component: RestaurantManagementComponent },
      { path: 'reports', component: AdminReportsComponent },
      { path: '', redirectTo: 'users', pathMatch: 'full' } // default child route
    ]  
  },
  
  { path: 'restaurant-dashboard', component: RestaurantDashboardComponent,
    children: [
      {path: 'menu', component:MenuComponent },
      {path:'orders', component:OrdersComponent},
      {path:'profile', component:RestaurantProfileComponent },
    ]
  },
  { path: 'restaurant-dashboard/create-restaurant', component: CreateRestaurantComponent },
  { path: 'customer-dashboard', component: CustomerDashboardComponent },
  {path:'my-orders',component:CustomerOrdersComponent},
  {path:'customer-address',component:CustomerAddressComponent},
  {path: 'restaurantmenu/:id', component:RestaurantMenuComponent },
  { path: 'order-summary', component: OrderSummaryComponent },
  { path: 'payment', component: PaymentComponent },
  { path: 'delivery-dashboard', component: DeliveryDashboardComponent },
  { path: '**', redirectTo: '' } // Redirect unknown paths to welcome
];