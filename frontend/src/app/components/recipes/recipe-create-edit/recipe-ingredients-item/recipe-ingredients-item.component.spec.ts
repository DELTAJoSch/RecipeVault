import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeIngredientsItemComponent } from './recipe-ingredients-item.component';

describe('RecipeIngredientsItemComponent', () => {
  let component: RecipeIngredientsItemComponent;
  let fixture: ComponentFixture<RecipeIngredientsItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecipeIngredientsItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeIngredientsItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
