import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router'; // ✅ Import this for router-outlet
import { HeaderComponent } from '../../components/header/header.component';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [RouterModule,HeaderComponent], // ✅ Include RouterModule here
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {

  fullName: string | null = '';

  ngOnInit(): void {
    this.fullName = localStorage.getItem('fullName');
  }
  logout() {
    localStorage.removeItem('fullName');
    localStorage.removeItem('roleID');
    localStorage.removeItem('token');
    window.location.href = '/login'; // Redirect to login page
  }

}
  
  
    