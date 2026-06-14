import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isAuthenticated: boolean = false;
  roles: any;
  username: any;
  accessToken: any;

  constructor(private http: HttpClient, private router: Router) { }

  public login(username: string, password: string) {
    const options = {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    };
    const params = new HttpParams()
      .set('username', username)
      .set('password', password);

    return this.http.post(environment.backendHost + '/login', params, options);
  }

  public loadProfile(data: any) {
    this.isAuthenticated = true;
    this.accessToken = data['access-token'];
    const decodedJwt: any = jwtDecode(this.accessToken);
    this.username = decodedJwt.sub;
    this.roles = decodedJwt.scope;
    window.localStorage.setItem('jwt-token', this.accessToken);
  }

  public logout() {
    this.isAuthenticated = false;
    this.accessToken = undefined;
    this.username = undefined;
    this.roles = undefined;
    window.localStorage.removeItem('jwt-token');
    this.router.navigateByUrl('/login');
  }

  public loadTokenFromLocalStorage() {
    const token = window.localStorage.getItem('jwt-token');
    if (token) {
      this.loadProfile({ 'access-token': token });
      this.router.navigateByUrl('/admin/customers');
    }
  }
}
