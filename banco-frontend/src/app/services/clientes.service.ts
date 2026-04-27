import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Cliente } from '../models/cliente.model';

@Injectable({
  providedIn: 'root'
})
export class ClientesService {

  private api = 'http://localhost:8080/clientes';

  constructor(private http: HttpClient) {}

  getClientes() {
    return this.http.get<Cliente[]>(this.api);
  }

  crearCliente(cliente: Cliente) {
    return this.http.post(this.api, cliente);
  }

  actualizarCliente(id: number, cliente: any) {
  return this.http.put(`${this.api}/${id}`, cliente);
}

eliminarCliente(id: number) {
  return this.http.delete(`${this.api}/${id}`);
}
}