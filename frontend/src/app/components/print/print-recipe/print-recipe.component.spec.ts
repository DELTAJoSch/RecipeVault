import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrintRecipeComponent } from './print-recipe.component';

describe('PrintRecipeComponent', () => {
  let component: PrintRecipeComponent;
  let fixture: ComponentFixture<PrintRecipeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PrintRecipeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PrintRecipeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
