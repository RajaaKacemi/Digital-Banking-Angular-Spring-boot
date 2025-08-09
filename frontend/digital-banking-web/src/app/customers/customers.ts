import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {CustomerService} from '../services/customer';
import {catchError, map, Observable, throwError} from 'rxjs';
import {Customer} from '../model/customer.model';
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {Router} from '@angular/router';

@Component({
  selector: 'app-customers',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customers.html',
  styleUrl: './customers.css'
})
export class Customers implements OnInit {
  customers!: Observable<Array<Customer>>;
  errorMessage!: string;
  searchFormGroup: FormGroup | undefined;
  constructor(private customerService : CustomerService, private fb : FormBuilder, private router : Router) {}

  ngOnInit(): void {
    this.searchFormGroup=this.fb.group({
      keyword : this.fb.control("")
    });
    this.customers=this.customerService.getCustomers().pipe(
      catchError(err => {
        this.errorMessage=err.message;
        return throwError(err);
    })
    );
  }

  handleSearchCustomers() {
    let kw=this.searchFormGroup?.value.keyword;
    this.customers=this.customerService.searchCustomer(kw).pipe(
        catchError(err => {
          this.errorMessage=err.message;
          return throwError(err);
        })
    );
  }

  handleDeleteCustomer(c : Customer) {
    let conf = confirm("Are you sure, you want to delete this customer");
    if(!conf) return;

    this.customerService.deleteCustomer(c.id).subscribe({
      next : (resp) => {
        this.customers=this.customers.pipe(
          map(data=>{
            let index = data.indexOf(c);
            data.slice(index, 1);
            return data;
          })
        );
      },
      error : err => {
        console.log(err);
      }
    })
  }

  handleCustomerAccounts(customer: Customer) {
    this.router.navigateByUrl("/customer-accounts/" + customer.id, {state : customer});
  }
}
