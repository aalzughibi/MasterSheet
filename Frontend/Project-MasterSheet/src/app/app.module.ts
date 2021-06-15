import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegistarComponent } from './registar/registar.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { UsersTableComponent } from './users-table/users-table.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistarComponent,
    ResetPasswordComponent,
    UsersTableComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
