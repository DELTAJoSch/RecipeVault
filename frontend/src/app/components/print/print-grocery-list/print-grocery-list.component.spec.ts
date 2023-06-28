import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrintGroceryListComponent } from './print-grocery-list.component';

describe('PrintGroceryListComponent', () => {
  let component: PrintGroceryListComponent;
  let fixture: ComponentFixture<PrintGroceryListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrintGroceryListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PrintGroceryListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
