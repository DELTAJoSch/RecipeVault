import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorCreateEditComponent } from './author-create-edit.component';

describe('AuthorCreateEditComponent', () => {
  let component: AuthorCreateEditComponent;
  let fixture: ComponentFixture<AuthorCreateEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AuthorCreateEditComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AuthorCreateEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
