import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FormBuilder, ReactiveFormsModule, Validators,FormGroup } from '@angular/forms';
import { ClientesService } from '../../services/clientes.service';
import { Cliente } from '../../models/cliente.model';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './clientes.html',
  styleUrl: './clientes.css'
})
export class Clientes implements OnInit {

  clientes = signal<Cliente[]>([]);
  busqueda = signal('');

  clienteForm!: FormGroup;

  mostrarFormulario = false;
  modoEdicion = false;
  clienteEditandoId: number | null = null;

  mensajeError = signal('');
  mensajeExito = signal('');
private fb = inject(FormBuilder);
private clientesService = inject(ClientesService);
 
  constructor(
    
  ) {


    this.clienteForm = this.fb.group({
    nombre: ['', [Validators.required]],
    genero: ['', [Validators.required]],
    edad: [null as number | null, [Validators.required, Validators.min(1)]],
    identificacion: ['', [Validators.required, Validators.pattern(/^\d+$/)]],
    direccion: ['', [Validators.required]],
    telefono: ['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    estado: [true, [Validators.required]]
  });

  }

  ngOnInit(): void {
    this.cargarClientes();
  }

  cargarClientes(): void {
    this.clientesService.getClientes().subscribe({
      next: (data) => this.clientes.set(data),
      error: () => this.mensajeError.set('Error al cargar clientes')
    });
  }

  clientesFiltrados = computed(() => {
    const texto = this.busqueda().trim().toLowerCase();

    return this.clientes().filter(c =>
      String(c.id ?? '').includes(texto) ||
      String(c.nombre ?? '').toLowerCase().includes(texto) ||
      String(c.genero ?? '').toLowerCase().includes(texto) ||
      String(c.identificacion ?? '').toLowerCase().includes(texto) ||
      String(c.direccion ?? '').toLowerCase().includes(texto) ||
      String(c.telefono ?? '').toLowerCase().includes(texto)
    );
  });

  abrirNuevo(): void {
    this.limpiarMensajes();
    this.mostrarFormulario = true;
    this.modoEdicion = false;
    this.clienteEditandoId = null;

    this.clienteForm.reset({
      nombre: '',
      genero: '',
      edad: null,
      identificacion: '',
      direccion: '',
      telefono: '',
      password: '',
      estado: true
    });
  }

  editarCliente(cliente: Cliente): void {
    this.limpiarMensajes();
    this.mostrarFormulario = true;
    this.modoEdicion = true;
    this.clienteEditandoId = cliente.id ?? null;

    this.clienteForm.patchValue({
      nombre: cliente.nombre,
      genero: cliente.genero,
      edad: cliente.edad,
      identificacion: cliente.identificacion,
      direccion: cliente.direccion,
      telefono: cliente.telefono,
      password: cliente.password ?? '',
      estado: cliente.estado
    });
  }

  guardarCliente(): void {
    this.limpiarMensajes();

    if (this.clienteForm.invalid) {
      this.clienteForm.markAllAsTouched();
      this.mensajeError.set('Revisa los campos del formulario');
      return;
    }

    const cliente: Cliente = {
      nombre: this.clienteForm.value.nombre!.trim(),
      genero: this.clienteForm.value.genero!.trim().toUpperCase(),
      edad: Number(this.clienteForm.value.edad),
      identificacion: this.clienteForm.value.identificacion!.trim(),
      direccion: this.clienteForm.value.direccion!.trim(),
      telefono: this.clienteForm.value.telefono!.trim(),
      password: this.clienteForm.value.password!.trim(),
      estado: Boolean(this.clienteForm.value.estado)
    };

    if (this.modoEdicion && this.clienteEditandoId) {
      this.actualizarCliente(this.clienteEditandoId, cliente);
    } else {
      this.crearCliente(cliente);
    }
  }

  crearCliente(cliente: Cliente): void {
    this.clientesService.crearCliente(cliente).subscribe({
      next: () => {
        this.mensajeExito.set('Cliente creado correctamente');
        this.cargarClientes();
        this.cancelarFormulario();
      },
      error: (err) => {
        console.log('Error creando cliente:', err);
        this.mensajeError.set(err.detalles || 'Error al crear cliente');
      }
    });
  }

  actualizarCliente(id: number, cliente: Cliente): void {
    this.clientesService.actualizarCliente(id, cliente).subscribe({
      next: () => {
        this.mensajeExito.set('Cliente actualizado correctamente');
        this.cargarClientes();
        this.cancelarFormulario();
      },
      error: (err) => {
        this.mensajeError.set(err.detalles || 'Error al actualizar cliente');
      }
    });
  }

  eliminarCliente(id: number): void {
    this.limpiarMensajes();

    if (!confirm('¿Está seguro de eliminar este cliente?')) {
      return;
    }

    this.clientesService.eliminarCliente(id).subscribe({
      next: () => {
        this.mensajeExito.set('Cliente eliminado correctamente');
        this.cargarClientes();
      },
      error: (err) => {
        this.mensajeError.set(err?.error || 'Error al eliminar cliente');
      }
    });
  }

  cancelarFormulario(): void {
    this.mostrarFormulario = false;
    this.modoEdicion = false;
    this.clienteEditandoId = null;
    this.clienteForm.reset({
      nombre: '',
      genero: '',
      edad: null,
      identificacion: '',
      direccion: '',
      telefono: '',
      password: '',
      estado: true
    });
  }

  campoInvalido(campo: string): boolean {
    const control = this.clienteForm.get(campo);
    return !!control && control.invalid && (control.touched || control.dirty);
  }

  private limpiarMensajes(): void {
    this.mensajeError.set('');
    this.mensajeExito.set('');
  }
}