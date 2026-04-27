import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Clientes } from './clientes';

import { of } from 'rxjs';
import { ClientesService } from '../../services/clientes.service';

describe('Clientes', () => {

  let component: Clientes;
  let fixture: ComponentFixture<Clientes>;

  const clientesServiceMock = {
    getClientes: () => of([]),
    crearCliente: () => of({}),
    actualizarCliente: () => of({}),
    eliminarCliente: () => of({})
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Clientes],
      providers: [
        { provide: ClientesService, useValue: clientesServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Clientes);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });


  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });


  it('debería cargar clientes al iniciar', () => {
    expect(component.clientes()).toEqual([]);
  });

  it('debería mostrar error si se intenta guardar sin datos', () => {

  component.mostrarFormulario = true;

  component.guardarCliente();

  expect(component.mensajeError()).toBeTruthy();
});

});