import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Customer} from '../model/customer.model';
import {environement} from "../../../environments/environement";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  constructor(private http:HttpClient) {}

  public getCustomers():Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(environement.backendHost + "/customers");
  }

  public searchCustomer(keyword: String):Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(environement.backendHost +"/customers/search?keyword="+keyword);
  }

  public saveCustomer(customer: Customer):Observable<Customer> {
    return this.http.post<Customer>(environement.backendHost +"/customers", customer);
  }

  public deleteCustomer(id: number){
    return this.http.delete<Customer>(environement.backendHost +"/customers/" + id);
  }
}
