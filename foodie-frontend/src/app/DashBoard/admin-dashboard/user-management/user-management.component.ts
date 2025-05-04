import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
  users: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadUsers();
  }
  

  loadUsers() {
    this.http.get<any[]>('http://localhost:8080/user/list').subscribe(data => {
      this.users = data;
    });
  }

  deleteUser(id: number) {
    if (confirm('Are you sure to delete this user?')) {
      this.http.delete(`http://localhost:8080/user/delete/${id}`).subscribe(() => {
        // this.loadUsers();
        this.users = this.users.filter(user => user.userID !== id);
      });
    }
  }
  getRoleName(roleID: number): string {
    switch (roleID) {
      case 1: return 'Admin';
      case 2: return 'Restaurant Admin';
      case 3: return 'Customer';
      case 4: return 'Delivery';
      default: return 'Unknown';
    }
  }
  
}
