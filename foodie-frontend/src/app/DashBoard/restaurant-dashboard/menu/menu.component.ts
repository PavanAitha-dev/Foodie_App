import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {
  menuForm!: FormGroup;
  menuItems: any[] = [];
  restaurantId: string | null = '';
  categories: string[] = ['Appetizers', 'Main Course', 'Desserts', 'Beverages'];
  itemId: string | null = '';

  constructor(private fb: FormBuilder, private http: HttpClient) {}

  ngOnInit(): void {
    this.restaurantId = localStorage.getItem('restaurantId');
    this.menuForm = this.fb.group({
      itemid: [''],
      name: ['', Validators.required],
      category: ['', Validators.required],
      description: [''],
      price: ['', Validators.required],
      isVegetarian: [false],
    });
    this.loadMenu();
  }

  loadMenu(): void {
    this.http.get(`http://localhost:8080/restaurant/allItems/${this.restaurantId}`).subscribe((items: any) => {
      this.menuItems = items;
    });
  }

  onSubmit(): void {
    if (this.menuForm.valid && this.restaurantId) {
      const payload = { ...this.menuForm.value, restaurantId: this.restaurantId , itemId: this.itemId };
      this.http.post(`http://localhost:8080/restaurant/addItem`, payload).subscribe(() => {
        this.menuForm.reset();
        this.loadMenu();
      });
    }
  }

  deleteItem(id: number): void {
    this.http.delete(`http://localhost:8080/restaurant/deleteItem/${id}`).subscribe(() => {
      this.loadMenu();
    });
  }
  editItem(item: any): void {
    this.itemId=item.itemId;
    this.menuForm.patchValue(item);

  }
}