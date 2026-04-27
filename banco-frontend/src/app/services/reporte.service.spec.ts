import { ComponentFixture, TestBed } from '@angular/core/testing';

import { of } from 'rxjs';
import { Reportes } from '../pages/reportes/reportes';
import { ClientesService } from './clientes.service';
import { ReportesService } from './reporte.service';

describe('Reportes', () => {

  let component: Reportes;
  let fixture: ComponentFixture<Reportes>;

  const reportesServiceMock = {
    getReporte: () => of([]),
    getReportePdf: () => of({ pdfBase64: '' })
  };

  const clientesServiceMock = {
    getClientes: () => of([])
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Reportes],
      providers: [
        { provide: ReportesService, useValue: reportesServiceMock },
        { provide: ClientesService, useValue: clientesServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Reportes);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debería crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('debería cargar clientes al iniciar', () => {
    expect(component.clientes()).toEqual([]);
  });

  it('debería generar reporte cuando los datos son válidos', () => {

    component.clienteId = 1;
    component.fechaInicio = '2026-04-25';
    component.fechaFin = '2026-04-25';

    component.generarReporte();

    expect(component.reporte()).toEqual([]);
  });

});