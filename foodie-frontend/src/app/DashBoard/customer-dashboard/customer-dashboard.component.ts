import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-customer-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './customer-dashboard.component.html',
  styleUrls: ['./customer-dashboard.component.css']
})
export class CustomerDashboardComponent implements OnInit {
  fullName: string | null = '';
  userId: number = 0;
  restaurants: any[] = [];
  cart: any[] = [];
  showCart = false;
  showProfileDropdown = false;
  searchQuery: string = '';
  fullRestaurantList: any[] = [];
  addresses: any[] = [];
  selectedAddress: string = '';
  showAddressDropdown = false;

  constructor(private http: HttpClient, private router: Router) {}

  toggleAddressDropdown() {
    this.showAddressDropdown = !this.showAddressDropdown;
  }

  formatAddress(address: any): string {
    return `${address.addressType} - ${address.address}, ${address.landMark}, ${address.pincode}`;
  }

  selectAddress(address: any) {
    this.selectedAddress = this.formatAddress(address);
    localStorage.setItem('customerAddress', this.selectedAddress); // Persist selection
    this.showAddressDropdown = false;
  }

  ngOnInit(): void {
    this.fullName = localStorage.getItem('fullName');
    this.userId = Number(localStorage.getItem('userId'));
    this.selectedAddress = localStorage.getItem('customerAddress') || ''; // Load saved address
    this.loadRestaurants();
    this.loadAddresses();
  }

  logout() {
    localStorage.clear();
    window.location.href = '';
  }

  toggleProfileDropdown() {
    this.showProfileDropdown = !this.showProfileDropdown;
  }

  loadRestaurants(): void {
    this.http.get<any[]>('http://localhost:8080/restaurant/all').subscribe((data) => {
      this.restaurants = data.filter(restaurant => restaurant.status === 'SERVING');
      this.fullRestaurantList = data.filter(fullRestaurantList => fullRestaurantList.status === 'SERVING');
    });
  }

  loadAddresses(): void {
    this.http.get<any[]>(`http://localhost:8080/user/getAddressById/${this.userId}`)
      .subscribe(data => {
        this.addresses = data;

        // If no address was selected before, default to the first one
        if (!this.selectedAddress && this.addresses.length > 0) {
          this.selectAddress(this.addresses[0]);
        }
      });
  }

  search(): void {
    const query = this.searchQuery.toLowerCase().trim();
    this.restaurants = query
      ? this.fullRestaurantList.filter(res =>
          res.name.toLowerCase().includes(query)
        )
      : this.fullRestaurantList;
  }

  openRestaurantMenu(restaurant: any) {
    localStorage.setItem('selectedRestaurant', JSON.stringify(restaurant));
    this.router.navigate(['/restaurantmenu', restaurant.id]);
  }
}
