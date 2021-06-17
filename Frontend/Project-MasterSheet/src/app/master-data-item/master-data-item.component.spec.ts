import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MasterDataItemComponent } from './master-data-item.component';

describe('MasterDataItemComponent', () => {
  let component: MasterDataItemComponent;
  let fixture: ComponentFixture<MasterDataItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MasterDataItemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MasterDataItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
