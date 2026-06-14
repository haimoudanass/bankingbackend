import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginFormGroup!: FormGroup;
  errorMessage: any;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.loginFormGroup = this.fb.group({
      username: this.fb.control(''),
      password: this.fb.control('')
    });
  }

  handleLogin() {
    const username = this.loginFormGroup.value.username;
    const password = this.loginFormGroup.value.password;

    this.authService.login(username, password).subscribe({
      next: data => {
        this.authService.loadProfile(data);
        this.router.navigateByUrl('/admin/customers');
      },
      error: err => {
        if (err.status === 0) {
          this.errorMessage = 'Impossible de joindre le serveur. Démarrez le backend : .\\mvnw.cmd spring-boot:run';
        } else if (err.status === 401 || err.status === 403) {
          this.errorMessage = 'Identifiants incorrects';
        } else {
          this.errorMessage = err.error?.message || err.message || 'Erreur de connexion';
        }
      }
    });
  }
}
