import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-customer-address',
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './customer-address.component.html',
  styleUrls: ['./customer-address.component.css']
})
export class CustomerAddressComponent implements OnInit {
  addressForm!: FormGroup;
  addresses: any[] = [];
  editingAddressId: number | null = null;
  userId: number = 0; // Replace with actual user ID

  constructor(private fb: FormBuilder, private http: HttpClient) {}

  ngOnInit(): void {
    this.addressForm = this.fb.group({
      address: ['', Validators.required],
      landMark: [''],
      pincode: ['', [Validators.required, Validators.pattern('^[0-9]{6}$')]],
      addressType: ['', Validators.required],
      userId: [] // Initialize userId here
    });
    this.userId = Number(localStorage.getItem('userId'));
    this.loadAddresses();
  }

  loadAddresses(): void {
    this.http.get<any[]>(`http://localhost:8080/user/getAddressById/${this.userId}`)
    .subscribe(data => {
      this.addresses = data;
    });
  }

  onSubmit(): void {
    const addressData = this.addressForm.value;
    addressData.userId = this.userId; // Set userId from local storage

    if (this.editingAddressId !== null) {
      this.http.put(`http://localhost:8080/user/editAddressById/${this.editingAddressId}`, addressData)
      .subscribe(() => {
        this.loadAddresses();
        this.resetForm();
      });
    } else {
      this.http.post('http://localhost:8080/user/addAddress', addressData).subscribe(() => {
        this.loadAddresses();
        this.resetForm();
      });
    }
  }

  editAddress(addr: any): void {
    this.editingAddressId = addr.id;
    console.log(this.editingAddressId);
    this.addressForm.patchValue(addr);
  }

  deleteAddress(id: number): void {
    this.http.delete(`http://localhost:8080/user/deleteAddress/${id}`).subscribe(() => {
      this.loadAddresses();
    });
  }
  resetForm(): void {
    this.addressForm.reset();
    this.editingAddressId = null;
  }
}
