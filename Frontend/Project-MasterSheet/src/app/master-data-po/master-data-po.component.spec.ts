import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MasterDataPoComponent } from './master-data-po.component';

describe('MasterDataPoComponent', () => {
  let component: MasterDataPoComponent;
  let fixture: ComponentFixture<MasterDataPoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MasterDataPoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MasterDataPoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
