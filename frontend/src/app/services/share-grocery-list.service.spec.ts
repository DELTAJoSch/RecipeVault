import {TestBed} from '@angular/core/testing';

import {ShareGroceryListService} from './share-grocery-list.service';


describe('ShareGroceryListService', () => {

  it('should be created', () => {
    const service: ShareGroceryListService = TestBed.inject(ShareGroceryListService);
    expect(service).toBeTruthy();
  });
});
