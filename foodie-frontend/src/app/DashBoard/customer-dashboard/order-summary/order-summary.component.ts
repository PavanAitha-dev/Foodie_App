import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-order-summary',
  imports:[CommonModule],
  templateUrl: './order-summary.component.html',
  styleUrls: ['./order-summary.component.css']
})
export class OrderSummaryComponent {
  cart: any[] = [];
  taxRate = 0.05; // 5% tax
  platformFee = 30; // ₹30 platform fee
  restaurantCharges = 20; // ₹20 restaurant charges
  deliveryCharges = 50; // ₹50 delivery charge
  GrandTotal: number = 0; // Initialize GrandTotal
  selectedAddress: string = '';// Load saved address

  constructor( private router: Router) {
    const nav = this.router.getCurrentNavigation();
    const state = nav?.extras?.state as { cart: any[] };
    this.cart = state?.cart || [];
  }
  ngOnInit(): void {
    this.selectedAddress = localStorage.getItem('customerAddress') || ''; // Load saved address
  }

  getItemsTotal(): number {
    return this.cart.reduce((total, item) => total + (item.price * item.quantity), 0);
  }

  getTax(): number {
    return this.getItemsTotal() * this.taxRate;
  }

  getGrandTotal(): number {
    this.GrandTotal= this.getItemsTotal() + this.getTax() + this.platformFee + this.restaurantCharges + this.deliveryCharges;
    return this.GrandTotal;
  }

  goToPaymentPage(): void {
     localStorage.setItem('grandTotal', this.GrandTotal.toString()); // Store GrandTotal in localStorage
     this.router.navigate(['/payment'], { state: {cart :this.cart} });
  }
}
