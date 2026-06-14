import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  title = 'Digital Banking';

  constructor(public authService: AuthService) {}

  ngOnInit(): void {
    if (window.location.pathname !== '/login') {
      const token = window.localStorage.getItem('jwt-token');
      if (token) {
        this.authService.loadProfile({ 'access-token': token });
      }
    }
  }

  handleLogout() {
    this.authService.logout();
  }

  isAdmin(): boolean {
    return this.authService.roles && this.authService.roles.includes('ADMIN');
  }
}
