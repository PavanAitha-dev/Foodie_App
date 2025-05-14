import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-restaurant-menu',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './restaurant-menu.component.html',
  styleUrls: ['./restaurant-menu.component.css']
})
export class RestaurantMenuComponent implements OnInit {
  restaurantId!: number;
  items: any[] = [];
  cart: any[] = [];
  restaurantDetails: any;
  cartVisible: boolean = true;

  constructor(private route: ActivatedRoute, private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    const stored = localStorage.getItem('selectedRestaurant');
    if (stored) {
      this.restaurantDetails = JSON.parse(stored);
      this.restaurantId = this.restaurantDetails.id;
    } else {
      this.restaurantId = +this.route.snapshot.paramMap.get('id')!;
    }
     const storedCart = localStorage.getItem(`cart+${this.restaurantId}`);
     this.cart = storedCart ? JSON.parse(storedCart) : [];
    localStorage.setItem('restaurantId', this.restaurantId.toString());
    // localStorage.removeItem('cart');

    this.loadItems();
  }

  loadItems() {
    this.http.get<any[]>(`http://localhost:8080/restaurant/allItems/${this.restaurantId}`)
      .subscribe(data => this.items = data);
  }

  addToCart(item: any) {
    const existingItem = this.cart.find(i => i.itemId === item.itemId);
    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      this.cart.push({ ...item, quantity: 1 });
    }
    localStorage.setItem(`cart+${this.restaurantId}`, JSON.stringify(this.cart));
    console.log('Cart updated:', this.cart);
  }

  removeFromCart(item: any) {
    this.cart = this.cart.filter(i => i.itemId !== item.itemId);
    localStorage.setItem(`cart+${this.restaurantId}`, JSON.stringify(this.cart));
    console.log('Item removed from cart:', item);
  }

  getTotal(): number {
    return this.cart.reduce((total, item) => total + (item.price * item.quantity), 0);
  }

  toggleCart(): void {
    this.cartVisible = !this.cartVisible;
  }
goToOrderPage() {
  this.router.navigate(['/order-summary'], { state: { cart: this.cart } });
}

}
