import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Observable, catchError, throwError } from 'rxjs';
import { Customer } from '../models/customer.model';
import { CustomerService } from '../services/customer.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit {
  customers$!: Observable<Array<Customer>>;
  errorMessage!: string;
  searchFormGroup!: FormGroup;

  constructor(
    private customerService: CustomerService,
    private fb: FormBuilder,
    public authService: AuthService
  ) { }

  isAdmin(): boolean {
    return this.authService.roles && this.authService.roles.includes('ADMIN');
  }

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword: this.fb.control('')
    });
    this.handleSearchCustomers();
  }

  handleSearchCustomers() {
    const kw = this.searchFormGroup?.value.keyword;
    this.customers$ = this.customerService.searchCustomers(kw).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(() => err);
      })
    );
  }

  handleDeleteCustomer(c: Customer) {
    const conf = confirm('Êtes-vous sûr de vouloir supprimer ?');
    if (!conf) return;
    this.customerService.deleteCustomer(c.id).subscribe({
      next: () => {
        this.handleSearchCustomers();
      },
      error: err => {
        console.log(err);
      }
    });
  }
}
