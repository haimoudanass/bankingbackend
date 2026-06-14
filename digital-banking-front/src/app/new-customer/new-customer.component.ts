import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-new-customer',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './new-customer.component.html',
  styleUrl: './new-customer.component.css'
})
export class NewCustomerComponent implements OnInit {
  newCustomerFormGroup!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.newCustomerFormGroup = this.fb.group({
      name: this.fb.control(null, [Validators.required, Validators.minLength(4)]),
      email: this.fb.control(null, [Validators.required, Validators.email])
    });
  }

  handleSaveCustomer() {
    const customer = this.newCustomerFormGroup.value;
    this.customerService.saveCustomer(customer).subscribe({
      next: () => {
        alert('Client enregistré avec succès !');
        this.router.navigateByUrl('/customers');
      },
      error: err => {
        console.log(err);
      }
    });
  }
}
